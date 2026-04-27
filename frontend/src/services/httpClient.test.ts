import { beforeEach, describe, expect, it, vi } from "vitest";
import ky from "ky";
import { httpClient } from "./httpClient";

vi.mock("ky", () => ({
  default: vi.fn(),
}));

const kyMock = vi.mocked(ky);

describe("httpClient", () => {
  beforeEach(() => {
    kyMock.mockReset();
  });

  it("vrati JSON odpoved pro application/json", async () => {
    kyMock.mockResolvedValue(
      new Response(JSON.stringify({ hello: "world" }), {
        status: 200,
        headers: { "content-type": "application/json" },
      }),
    );

    const result = await httpClient<{ hello: string }>({
      url: "/hello",
      method: "GET",
    });

    expect(result).toEqual({ hello: "world" });
  });

  it("vrati text pro text/plain", async () => {
    kyMock.mockResolvedValue(
      new Response("Ahoj", {
        status: 200,
        headers: { "content-type": "text/plain" },
      }),
    );

    const result = await httpClient<string>({
      url: "/hello",
      method: "GET",
    });

    expect(result).toBe("Ahoj");
  });

  it("vrati undefined pro 204 bez tela", async () => {
    kyMock.mockResolvedValue(
      new Response(null, {
        status: 204,
      }),
    );

    const result = await httpClient<void>({
      url: "/hello",
      method: "GET",
    });

    expect(result).toBeUndefined();
  });

  it("vyhodi textovou chybu pro ne-JSON error response", async () => {
    kyMock.mockResolvedValue(
      new Response("Neplatny pozadavek", {
        status: 400,
        headers: { "content-type": "text/plain" },
      }),
    );

    await expect(
      httpClient<void>({
        url: "/hello",
        method: "GET",
      }),
    ).rejects.toThrow("Neplatny pozadavek");
  });
});
